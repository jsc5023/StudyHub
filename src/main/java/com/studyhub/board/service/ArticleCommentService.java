package com.studyhub.board.service;

import com.studyhub.board.domain.Article;
import com.studyhub.board.domain.ArticleComment;
import com.studyhub.board.domain.UserAccount;
import com.studyhub.board.dto.ArticleCommentDto;
import com.studyhub.board.repository.ArticleCommentRepository;
import com.studyhub.board.repository.ArticleRepository;
import com.studyhub.board.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleCommentService {
    private final UserAccountRepository userAccountRepository;
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> searchArticleComments(Long articleId) {
        return articleCommentRepository.findByArticle_Id(articleId)
                .stream()
                .map(ArticleCommentDto::from)
                .toList();
    }

    public void saveArticleComment(ArticleCommentDto dto) {
        try {
            Article article = articleRepository.getReferenceById(dto.articleId());
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
            ArticleComment articleComment = dto.toEntity(article, userAccount);

            if (dto.parentCommentId() != null) {
                ArticleComment parentComment = articleCommentRepository.getReferenceById(dto.parentCommentId());
                parentComment.addChildComment(articleComment);
            } else {
                articleCommentRepository.save(articleComment);
            }
        } catch (EntityNotFoundException e){
            log.warn("댓글 저장 실패. 댓글 작성에 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }

    public void updateArticleComment(ArticleCommentDto dto) {
        try {
            ArticleComment articleComment = articleCommentRepository.getReferenceById(dto.id());
            if(dto.content() != null) { articleComment.setContent(dto.content()) ;}
        } catch (EntityNotFoundException e){
            log.warn("댓글 업데이트 실패. 댓글을 찾을 수 없습니다 - dto {}", dto);
        }

    }

    public void deleteArticleComment(Long articleCommentId, String userId){
        articleCommentRepository.deleteByIdAndUserAccount_UserId(articleCommentId, userId);
    }
}
