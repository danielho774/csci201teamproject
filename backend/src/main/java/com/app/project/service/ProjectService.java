//version 1 of ProjectService
@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private UserRepository userRepo;

    public Project createProject(String ownerEmail, String projectName) {
        User owner = userRepo.findById(ownerEmail).orElseThrow();
        Project project = new Project();
        project.setName(projectName);
        project.setOwner(owner);
        project.getMembers().add(owner);
        return projectRepo.save(project);
    }

    public void addMember(Long projectId, String userEmail) {
        Project project = projectRepo.findById(projectId).orElseThrow();
        User user = userRepo.findById(userEmail).orElseThrow();
        project.getMembers().add(user);
        projectRepo.save(project);
    }

    public void leaveProject(Long projectId, String userEmail) {
        Project project = projectRepo.findById(projectId).orElseThrow();
        User user = userRepo.findById(userEmail).orElseThrow();

        if (user.getEmail().equals(project.getOwner().getEmail())) {
            throw new IllegalStateException("Owner must reassign ownership before leaving.");
        }

        project.getMembers().remove(user);
        projectRepo.save(project);
    }

    public void deleteProject(Long projectId, String ownerEmail) {
        Project project = projectRepo.findById(projectId).orElseThrow();
        if (!project.getOwner().getEmail().equals(ownerEmail)) {
            throw new AccessDeniedException("Only the owner can delete the project.");
        }
        projectRepo.delete(project);
    }

    public void reassignOwner(Long projectId, String currentOwnerEmail, String newOwnerEmail) {
        Project project = projectRepo.findById(projectId).orElseThrow();
        if (!project.getOwner().getEmail().equals(currentOwnerEmail)) {
            throw new AccessDeniedException("Only the current owner can reassign ownership.");
        }

        User newOwner = userRepo.findById(newOwnerEmail).orElseThrow();
        if (!project.getMembers().contains(newOwner)) {
            throw new IllegalArgumentException("New owner must be a project member.");
        }

        project.setOwner(newOwner);
        projectRepo.save(project);
    }

    public void ownerLeaving(Long projectId, String currentOwnerEmail, String newOwnerEmail) {
        reassignOwner(projectId, currentOwnerEmail, newOwnerEmail);
        leaveProject(projectId, currentOwnerEmail);
    }
}

