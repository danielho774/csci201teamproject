@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Optional: find all projects for a user (owner or member)
    List<Project> findByOwnerEmail(String ownerEmail);

    @Query("SELECT p FROM Project p JOIN p.members m WHERE m.email = :email")
    List<Project> findByMemberEmail(@Param("email") String email);
}
