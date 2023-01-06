package com.lodong.spring.supermandiarycustomer.repository;

import com.lodong.spring.supermandiarycustomer.domain.exhibition.ExhibitionComment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ExhibitionCommentRepository extends JpaRepository<ExhibitionComment, String> {
    @EntityGraph(value = "get-with-all-exhibiton-comment", type = EntityGraph.EntityGraphType.LOAD)
    public Optional<List<ExhibitionComment>> findByExhibitionBoard_IdAndSequence(String id, int sequence);
    @EntityGraph(value = "get-with-all-exhibiton-comment", type = EntityGraph.EntityGraphType.LOAD)
    public Optional<List<ExhibitionComment>> findByCommentGroupIdAndSequenceNot(String commentGroupId, int sequence);

    @EntityGraph(value = "get-with-all-exhibiton-comment", type = EntityGraph.EntityGraphType.LOAD)
    public Optional<List<ExhibitionComment>> findByCommentGroupIdAndSequence(String commentGroupId, int sequence);

    @Transactional
    @Modifying
    @Query(value = "UPDATE ExhibitionComment u set u.hasCommentGroup =:status where u.commentGroupId = :commentGroupId and u.sequence = 0")
    public void updateHasCommentGroup(boolean status, String commentGroupId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE ExhibitionComment u set u.comment =:comment where u.userCustomer.id =:userId and u.id =:commentId")
    public void updateComment(String comment, String userId, String commentId);


    @EntityGraph(value = "get-with-all-exhibiton-comment", type = EntityGraph.EntityGraphType.LOAD)
    public Optional<ExhibitionComment> findByUserCustomer_IdAndId(String id, String commentId);

}
