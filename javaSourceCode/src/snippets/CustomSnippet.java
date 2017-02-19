package snippets;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.ibm.team.repository.client.IItemManager;
import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.client.TeamPlatform;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.common.UUID;
import com.ibm.team.scm.client.IWorkspaceConnection;
import com.ibm.team.scm.client.SCMPlatform;
import com.ibm.team.scm.client.internal.ClientChangeSetEntry;
import com.ibm.team.scm.common.IChangeSet;
import com.ibm.team.scm.common.IComponent;
import com.ibm.team.scm.common.IComponentHandle;
import com.ibm.team.scm.common.IWorkspace;
import com.ibm.team.scm.common.IWorkspaceHandle;
import com.ibm.team.scm.common.dto.IWorkspaceSearchCriteria;

/*
 * Gets the UUID for the latest changeset for a workspace component that is check-in and delivered. 
 * 
 */

public class CustomSnippet {

	private static String USER = "";
	private static String PASSWORD = ""; 

	@SuppressWarnings("unchecked")
	public static String getLatestChangesetUUID(String repoAddress, String user, String password, String workspaceName) throws TeamRepositoryException {
		String changesetUUID = "";
		IProgressMonitor monitor = new SysoutProgressMonitor();
		TeamPlatform.startup();
		try {     
			long timeValue = 0;

			ITeamRepository repo = login(monitor, repoAddress, user, password);
			IWorkspaceConnection conn = findWorkspace(repo, workspaceName, monitor);
			if (conn == null) {
				throw new TeamRepositoryException("Cannot get connection to workspace");
			}
			for (IComponentHandle cmp : (List<IComponentHandle>)conn.getComponents())
			{
				for (ClientChangeSetEntry chng : (List<ClientChangeSetEntry>)conn.changeHistory(cmp).recent(monitor))
				{
					IChangeSet cs = (IChangeSet)repo.itemManager().fetchCompleteItem(chng.changeSet(), IItemManager.DEFAULT, null);
					if (timeValue < chng.creationDate().getTime() && !cs.isActive() && cs.isComplete()) 
					{
						timeValue = chng.creationDate().getTime();
						changesetUUID = chng.changeSet().getItemId().getUuidValue();
					}
				}
			}

		} finally {
			TeamPlatform.shutdown();
		}
		return changesetUUID;
	}

	private static ITeamRepository login(IProgressMonitor monitor, String repoAddress, String user, String password) throws TeamRepositoryException {
		USER = user;
		PASSWORD = password;
		ITeamRepository repository = TeamPlatform.getTeamRepositoryService().getTeamRepository(repoAddress);
		repository.registerLoginHandler(new ITeamRepository.ILoginHandler() {
			public ILoginInfo challenge(ITeamRepository repository) {
				return new ILoginInfo() {
					public String getUserId() {
						return USER;
					}
					public String getPassword() {
						return PASSWORD;                        
					}
				};
			}
		});
		monitor.subTask("Contacting " + repository.getRepositoryURI() + "...");
		repository.login(monitor);
		monitor.subTask("Connected");
		return repository;
	}

	private static IWorkspaceConnection findWorkspace(ITeamRepository repo, String workspace, IProgressMonitor monitor) throws TeamRepositoryException {
		IWorkspaceHandle wh;
		UUID workspaceId = toUUID(workspace);
		if (workspaceId != null) {
			wh = (IWorkspaceHandle)IWorkspace.ITEM_TYPE.createItemHandle(workspaceId, null);
		} else {
			//not a UUID, could be a name.
			IWorkspaceSearchCriteria wsc = IWorkspaceSearchCriteria.FACTORY.newInstance();
			wsc.setExactName(workspace);
			wsc.setKind(IWorkspaceSearchCriteria.ALL);
			List<IWorkspaceHandle> ws = SCMPlatform.getWorkspaceManager(repo).findWorkspaces(wsc, 1, monitor);
			switch (ws.size()) {
			case 0:
				wsc = IWorkspaceSearchCriteria.FACTORY.newInstance();
				wsc.setExactName(workspace);
				ws = SCMPlatform.getWorkspaceManager(repo).findWorkspaces(wsc, 2, monitor);
				switch (ws.size()) {
				case 0:
					System.err.println("Failed to find workspace named " + workspace);
					return null;
				case 1:
					wh = ws.get(0);
					break;
				default:
					System.err.println("Multiple workspaces named " + workspace);
					return null;
				}
			case 1:
				wh = ws.get(0);
				break;
			default:
				System.err.println("Multiple workspaces named " + workspace + " owned by " + repo.loggedInContributor().getUserId());
				return null;
			}
		}
		return SCMPlatform.getWorkspaceManager(repo).getWorkspaceConnection(wh, monitor);
	}

	private static UUID toUUID(String id) {
		try {
			return UUID.valueOf(id);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}


	public static void main(String[] args) throws TeamRepositoryException {
		{
			System.out.println(getLatestChangesetUUID("https://jazz.net/sandbox01-ccm", "amitmohleji", "abcd1234", "amitstream"));
			System.out.println(getLatestChangesetUUID("https://jazz.net/sandbox01-ccm", "amitmohleji", "abcd1234", "petclinic"));
		}
	}
}
