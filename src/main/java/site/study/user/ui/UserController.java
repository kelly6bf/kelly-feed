package site.study.user.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.study.common.ui.Response;
import site.study.user.application.UserService;
import site.study.user.application.dto.CreateUserRequestDto;
import site.study.user.application.dto.GetUserListResponseDto;
import site.study.user.application.dto.GetUserResponseDto;
import site.study.user.domain.User;
import site.study.user.repository.jpa.JpaUserListQueryRepository;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JpaUserListQueryRepository userListEntityQuery;

    @PostMapping
    public Response<Long> createUser(@RequestBody CreateUserRequestDto dto) {
        User user = userService.createUser(dto);
        return Response.ok(user.getId());
    }

    @GetMapping("/{userId}")
    public Response<GetUserResponseDto> getUserResponse(@PathVariable(name="userId") Long id) {
        return Response.ok(userService.getUserProfile(id));
    }

    @GetMapping("/{userId}/follower")
    public Response<List<GetUserListResponseDto>> getFollowerList(@PathVariable(name="userId") Long id) {
        return Response.ok(userListEntityQuery.getFollowerList(id));
    }

    @GetMapping("/{userId}/following")
    public Response<List<GetUserListResponseDto>> getFollowingList(@PathVariable(name="userId") Long id) {
        return Response.ok(userListEntityQuery.getFollowingList(id));
    }
}
