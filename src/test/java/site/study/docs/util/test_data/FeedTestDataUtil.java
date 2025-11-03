package site.study.docs.util.test_data;

import site.study.post.application.dto.GetCommentContentResponseDto;
import site.study.post.application.dto.GetPostContentResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public class FeedTestDataUtil {

    public static List<GetPostContentResponseDto> createFeedResponseData() {
        return List.of(
            GetPostContentResponseDto.builder()
                .id(11L)
                .content("오늘 치킨 먹었어요 ㅎㅎ")
                .userId(2L)
                .userName("써니")
                .userProfileImage("https://image.site")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .likeCount(2)
                .isLikedByMe(true)
                .commentCount(3)
                .build(),
            GetPostContentResponseDto.builder()
                .id(12L)
                .content("오늘 러닝하기 딱 좋은 날씨!!")
                .userId(2L)
                .userName("써니")
                .userProfileImage("https://image.site")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .likeCount(10)
                .isLikedByMe(false)
                .commentCount(10)
                .build(),
            GetPostContentResponseDto.builder()
                .id(13L)
                .content("새로 장만한 키보드 자랑!")
                .userId(2L)
                .userName("써니")
                .userProfileImage("https://image.site")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .likeCount(20)
                .isLikedByMe(true)
                .commentCount(5)
                .build()
        );
    }

    public static List<GetCommentContentResponseDto> createFeedCommentResponseData() {
        return List.of(
            GetCommentContentResponseDto.builder()
                .id(11L)
                .content("헐 저도 먹을래요!!!")
                .userId(2L)
                .userName("써니")
                .userProfileImage("https://image.site")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .likeCount(2)
                .isLikedByMe(true)
                .build(),
            GetCommentContentResponseDto.builder()
                .id(12L)
                .content("왜 혼자 먹어요???")
                .userId(3L)
                .userName("커밋")
                .userProfileImage("https://image.site")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .likeCount(10)
                .isLikedByMe(false)
                .build(),
            GetCommentContentResponseDto.builder()
                .id(13L)
                .content("다이어트는?")
                .userId(4L)
                .userName("엘모")
                .userProfileImage("https://image.site")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .likeCount(20)
                .isLikedByMe(false)
                .build()
        );
    }
}
