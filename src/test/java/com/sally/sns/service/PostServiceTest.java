package com.sally.sns.service;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sally.sns.controller.reuqest.CommentRequest;
import com.sally.sns.controller.reuqest.PostRequest;
import com.sally.sns.exception.ErrorCode;
import com.sally.sns.exception.SnsApplicationException;
import com.sally.sns.fixture.entity.TestPostEntity;
import com.sally.sns.fixture.request.PostRequestFactory;
import com.sally.sns.model.Member;
import com.sally.sns.model.entity.CommentEntity;
import com.sally.sns.model.entity.PostEntity;
import com.sally.sns.repository.CommentEntityRepository;
import com.sally.sns.repository.PostEntityRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
	public static final String TEST_TITLE = "testTitle";
	public static final String TEST_CONTENT = "testContentasdfjkl;";
	public static final String TEST_USER_NICKNAME = "testUserNickname";
	public static final String TEST_COMMENT = "코멘트가 담겨 있습니다.";
	@InjectMocks
	private PostService postService;
	@Mock
	private PostEntityRepository postEntityRepository;
	@Mock
	private UserService userService;
	@Mock
	private CommentEntityRepository commentEntityRepository;
	@Mock
	private AlarmService alarmService;

	private TestPostEntity testPostEntity;

	@BeforeEach
	void beforeEach() {
		testPostEntity = TestPostEntity.of(TEST_TITLE, TEST_CONTENT);
	}

	@Test
	@DisplayName("포스트 등록은 정상 동작 한다.")
	void post_createWithoutLocation_ok() {
		PostRequest.Creation postCreationRequest = getPostCreationRequest(PostRequestFactory.Designation.NOT_EMPTY);
		when(userService.getThatIfMember(TEST_USER_NICKNAME)).thenReturn(mock(Member.class));

		postService.create(postCreationRequest, TEST_USER_NICKNAME);
	}

	@Test
	@DisplayName("포스트 등록은 가입된 회원이 아니면 에러발생 한다.")
	void post_createInvalidUser_error() {
		doThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUNDED))
			.when(userService).getThatIfMember(TEST_USER_NICKNAME);

		assertThatThrownBy(
			() -> postService.create(getPostCreationRequest(PostRequestFactory.Designation.EMPTY), TEST_USER_NICKNAME))
			.isInstanceOf(SnsApplicationException.class)
			.hasMessageContaining(ErrorCode.USER_NOT_FOUNDED.name());
	}

	@Test
	@DisplayName("포스트 목록 조회가 정상 동작 한다.")
	void readAll_valid_ok() {
		Pageable pageable = mock(Pageable.class);
		when(postEntityRepository.findPostViews(pageable))
			.thenReturn(Page.empty());  // Collections.emptyList()

		postService.readAll(pageable);

		assertThatNoException();
	}

	@Test
	@DisplayName("나의 포스트 목록 조회가 정상 동작 한다.")
	void readMemberPosts_valid_ok() {
		Pageable pageable = mock(Pageable.class);
		when(postEntityRepository.findAllByAuthorId(testPostEntity.authorId(), pageable))
			.thenReturn(Page.empty());

		postService.readMemberPosts(pageable, testPostEntity.authorId());

		assertThatNoException();
	}

	@Test
	@DisplayName("포스트 수정기능은 정상 동작한다")
	void modify_valid_ok() {
		when(postEntityRepository.findWithAuthorById(any()))
			.thenReturn(Optional.of(mock(PostEntity.class)));

		postService.modify(getModificationRequest(), testPostEntity.postId(), testPostEntity.authorId());

		assertThatNoException();
	}

	@Test
	@DisplayName("포스트 수정시 해당 포스트가 없는 경우 에러 발생한다")
	void modify_noExistPost_error() {
		when(postEntityRepository.findWithAuthorById(any()))
			.thenReturn(Optional.empty());

		assertThatThrownBy(
			() -> postService.modify(getModificationRequest(), testPostEntity.postId(), testPostEntity.authorId()))
			.isInstanceOf(SnsApplicationException.class)
			.hasMessageContaining(ErrorCode.POST_NOT_FOUND.name());
	}

	@Test
	@DisplayName("포스트 수정시 포스트의 작성자가 아닌 경우 에러 발생한다")
	void modify_differentRequesterOfAuthor_error() {
		PostEntity postEntity = mock(PostEntity.class);
		Long expectedNotWriter = testPostEntity.authorId() + 1L;
		when(postEntityRepository.findWithAuthorById(testPostEntity.postId()))
			.thenReturn(Optional.of(postEntity));
		when(postEntity.isNotAuthor(expectedNotWriter)).thenReturn(true);

		assertThatThrownBy(
			() -> postService.modify(getModificationRequest(), testPostEntity.postId(), expectedNotWriter))
			.isInstanceOf(SnsApplicationException.class)
			.hasMessageContaining(ErrorCode.INVALID_AUTHORIZATION.name());
	}

	@Test
	@DisplayName("포스트 삭제는 정상 동작한다.")
	void deleteSoftly_valid_ok() {
		when(postEntityRepository.findWithAuthorById(testPostEntity.postId()))
			.thenReturn(Optional.of(mock(PostEntity.class)));
		postService.deleteSoftly(testPostEntity.postId(), testPostEntity.authorId());
	}

	@Test
	@DisplayName("포스트 삭제시 조회된 포스트가 없으면 에러 발생한다.")
	void deleteSoftly_emptyOfPost_error() {
		when(postEntityRepository.findWithAuthorById(testPostEntity.postId()))
			.thenReturn(Optional.empty());

		assertThatThrownBy(
			() -> postService.deleteSoftly(testPostEntity.postId(), testPostEntity.authorId()))
			.isInstanceOf(SnsApplicationException.class)
			.hasMessageContaining(ErrorCode.POST_NOT_FOUND.name());
	}

	@Test
	@DisplayName("포스트 삭제시 요청자와 작성자 일치하지 않으면 에러 발생한다")
	void deleteSoftly_invalidNotAuthor_error() {
		PostEntity postEntity = mock(PostEntity.class);
		Long expectedNotWriter = testPostEntity.authorId() + 1L;

		when(postEntityRepository.findWithAuthorById(testPostEntity.postId()))
			.thenReturn(Optional.of(postEntity));
		when(postEntity.isNotAuthor(expectedNotWriter)).thenReturn(true);

		assertThatThrownBy(
			() -> postService.deleteSoftly(testPostEntity.postId(), expectedNotWriter))
			.isInstanceOf(SnsApplicationException.class)
			.hasMessageContaining(ErrorCode.INVALID_AUTHORIZATION.name());
	}

	@Test
	@DisplayName("코멘트 저장이 정상 동작한다.")
	void createComment_valid_ok() {
		when(postEntityRepository.findById(any())).thenReturn(Optional.of(mock(PostEntity.class)));
		when(userService.getThatIfMember(any())).thenReturn(mock(Member.class));
		postService.createComment(testPostEntity.postId(), getCommentCreationRequest(), testPostEntity.getNickname());

		assertThatNoException();
		verify(commentEntityRepository, times(1)).save(any());
		verify(alarmService, times(1)).storeCommentAlarm(any(), any());
	}

	@Test
	@DisplayName("코멘트 저장은 작성된 포스트가 없으면 에러발생 한다.")
	void createComment_emptyPost_error() {
		when(postEntityRepository.findById(any())).thenReturn(Optional.empty());

		assertThatThrownBy(
			() -> postService.createComment(testPostEntity.postId(), getCommentCreationRequest(),
				testPostEntity.getNickname()))
			.isInstanceOf(SnsApplicationException.class)
			.hasMessageContaining(ErrorCode.POST_NOT_FOUND.name());
	}

	@Test
	@DisplayName("코멘트 저장은 회원이 아니면 에러 발생한다.")
	void createComment_notMember_error() {
		when(postEntityRepository.findById(any())).thenReturn(Optional.of(mock(PostEntity.class)));
		when(userService.getThatIfMember(any())).thenThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUNDED));

		assertThatThrownBy(
			() -> postService.createComment(testPostEntity.postId(), getCommentCreationRequest(),
				testPostEntity.getNickname()))
			.isInstanceOf(SnsApplicationException.class)
			.hasMessageContaining(ErrorCode.USER_NOT_FOUNDED.name());
	}

	@Test
	@DisplayName("코멘트 목록 조회는 정상 동작한다.")
	void readComments_valid_ok() {
		Pageable pageable = mock(Pageable.class);
		when(commentEntityRepository.findCommentViewsBy(any(), any())).thenReturn(Page.empty());

		postService.readComments(testPostEntity.postId(), pageable);

		assertThatNoException();
	}

	@Test
	@DisplayName("코멘트 수정은 정상 동작한다.")
	void modifyComment_valid_ok() {
		when(commentEntityRepository.findById(any())).thenReturn(Optional.of(mock(CommentEntity.class)));

		postService.modifyComment(testPostEntity.postId(), 1L, new CommentRequest.Modification("댓글 수정"),
			testPostEntity.authorId());

		assertThatNoException();
	}

	@Test
	@DisplayName("코멘트 수정은 작성된 포스트 없으면 에러 처리한다.")
	void modifyComment_notFountPost_error() {
		CommentEntity commentEntity = mock(CommentEntity.class);
		when(commentEntityRepository.findById(any())).thenReturn(Optional.of(commentEntity));
		when(commentEntity.hasNotPost(any())).thenReturn(true);

		assertThatThrownBy(
			() -> postService.modifyComment(testPostEntity.postId(), 1L, new CommentRequest.Modification("댓글 수정"),
				testPostEntity.authorId()))
			.isInstanceOf(SnsApplicationException.class)
			.hasMessageContaining(ErrorCode.INVALID_AUTHORIZATION.name());
	}

	@Test
	@DisplayName("코멘트 수정은 수정 요청자가 댓글 작성자가 아니면 에러 처리한다.")
	void modifyComment_differentCommentAuthor_error() {
		CommentEntity commentEntity = mock(CommentEntity.class);
		when(commentEntityRepository.findById(any())).thenReturn(Optional.of(commentEntity));
		when(commentEntity.isNotCommenter(any())).thenReturn(true);

		assertThatThrownBy(
			() -> postService.modifyComment(testPostEntity.postId(), 1L,
				new CommentRequest.Modification("댓글 수정"),
				testPostEntity.authorId()))
			.isInstanceOf(SnsApplicationException.class)
			.hasMessageContaining(ErrorCode.INVALID_AUTHORIZATION.name());
	}

	private PostRequest.Creation getPostCreationRequest(PostRequestFactory.Designation designation) {
		return PostRequestFactory.getPostCreationRequest(TEST_TITLE, TEST_CONTENT, designation);
	}

	private PostRequest.Modification getModificationRequest() {
		return new PostRequest.Modification(TEST_TITLE + "수정", TEST_CONTENT + "수정");
	}

	private CommentRequest.Creation getCommentCreationRequest() {
		return new CommentRequest.Creation(TEST_COMMENT);
	}
}
