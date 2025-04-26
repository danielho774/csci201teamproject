@Entity
@Table(name = "Tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private int taskID; 

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false) 
    private Project project; 

    @Column(name = "task_name", length = 25, nullable = false) 
    private String taskName; 

    @Column(name = "task_descrip", length = 150)
    private String taskDescrip; 

    @ManyToOne
    @JoinColumn(name = "status_type")
    private Status status; 

    @ManyToOne
    @JoinColumn(name = "priority") 
    private Priority priority; 

    @Column(name = "start_date")
    private LocalDate startDate; 

    @Column(name = "end_date")
    private LocalDate endDate; 

    @Column(name = "duration")
    private int duration; 

    @Column(name = "assigned")
    private boolean assigned;
    
    @ManyToMany(mappedBy = "tasks")
    private List<User> assignees = new ArrayList<>();
    
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    // Constructor, getters, and setters
    
    public Task() {
        // Default constructor
    }
    
    public Task(Project project, String taskName, String taskDescrip, 
                Status status, Priority priority, LocalDate startDate, 
                LocalDate endDate, int duration, boolean assigned) {
        this.project = project; 
        this.taskName = taskName; 
        this.taskDescrip = taskDescrip; 
        this.status = status; 
        this.priority = priority; 
        this.startDate = startDate; 
        this.endDate = endDate; 
        this.duration = duration; 
        this.assigned = assigned; 
    }
    
    // Required methods
    public boolean updateStatus(Status status) {
        this.status = status;
        return true;
    }
    
    public boolean addComment(Comment comment) {
        if (comment != null) {
            comment.setTask(this);
            comments.add(comment);
            return true;
        }
        return false;
    }
    
    public boolean addAssignee(User user) {
        if (user != null && !assignees.contains(user)) {
            assignees.add(user);
            this.assigned = true;
            return true;
        }
        return false;
    }
    
    public boolean removeAssignee(User user) {
        if (user != null && assignees.contains(user)) {
            assignees.remove(user);
            if (assignees.isEmpty()) {
                this.assigned = false;
            }
            return true;
        }
        return false;
    }
    
    // Getters and Setters
    public int getTaskID() {
        return taskID;
    }
    
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
    
    public Project getProject() {
        return project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    
    public String getTaskName() {
        return taskName;
    }
    
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    
    public String getTaskDescrip() {
        return taskDescrip;
    }
    
    public void setTaskDescrip(String taskDescrip) {
        this.taskDescrip = taskDescrip;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public Priority getPriority() {
        return priority;
    }
    
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public boolean isAssigned() {
        return assigned;
    }
    
    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
    
    public List<User> getAssignees() {
        return assignees;
    }
    
    public void setAssignees(List<User> assignees) {
        this.assignees = assignees;
        this.assigned = !assignees.isEmpty();
    }
    
    public List<Comment> getComments() {
        return comments;
    }
    
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}