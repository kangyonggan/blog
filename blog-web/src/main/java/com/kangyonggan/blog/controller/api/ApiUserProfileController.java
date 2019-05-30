package com.kangyonggan.blog.controller.api;

import com.kangyonggan.blog.annotation.PermissionMenu;
import com.kangyonggan.blog.controller.BaseController;
import com.kangyonggan.blog.dto.Response;
import com.kangyonggan.blog.model.UserProfile;
import com.kangyonggan.blog.service.system.UserProfileService;
import com.kangyonggan.blog.service.system.UserService;
import com.kangyonggan.blog.util.FileHelper;
import com.kangyonggan.blog.util.FileUpload;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author kangyonggan
 * @since 2019-05-28
 */
@RestController
@RequestMapping("api/user/profile")
@Api(tags = "ApiUserProfileController", description = "用户相关接口")
public class ApiUserProfileController extends BaseController {

    @Autowired
    private FileHelper fileHelper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileService userProfileService;

    /**
     * 上传头像
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("avatar")
    @ApiOperation("上传头像")
    @PermissionMenu("USER_PROFILE")
    public Response list(MultipartFile file) throws Exception {
        Response response = successResponse();

        if (file == null || file.isEmpty()) {
            return response.failure("图片内容为空！");
        }
        // 文件类型校验
        String ext = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
        if (!FileUpload.PICTURE_EXT.contains(ext)) {
            return response.failure("请选择jpg、jpeg、png或者gif格式的图片");
        }

        // 上传到本地
        String fileName = fileHelper.genFileName("avatar");
        FileUpload.upload(fileHelper.getFileUploadPath() + "avatar/", fileName, file);

        // 更新用户信息
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(currentUserId());
        userProfile.setAvatar("avatar/" + fileName + "." + ext);
        userProfileService.updateUserProfile(userProfile);

        response.put("user", userService.findUserDtoById(currentUserId()));
        return response;
    }

}
