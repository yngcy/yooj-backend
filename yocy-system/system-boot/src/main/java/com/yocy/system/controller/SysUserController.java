package com.yocy.system.controller;

/**
 * 用户控制器
 *
 * @author <a href="https://github.com/yngcy">YounGCY</a>
 */

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yocy.system.dto.UserAuthInfo;
import com.yocy.common.result.PageResult;
import com.yocy.common.result.Result;
import com.yocy.common.web.annotation.PreventDuplicateResubmit;
import com.yocy.system.listener.excel.UserImportListener;
import com.yocy.system.model.entity.SysUser;
import com.yocy.system.model.form.UserForm;
import com.yocy.system.model.form.UserRegisterForm;
import com.yocy.system.model.query.UserPageQuery;
import com.yocy.system.model.vo.*;
import com.yocy.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@Tag(name = "用户接口")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    @Operation(summary = "用户分页列表")
    @GetMapping("/page")
    public PageResult<UserPageVO> getUserPage(@ParameterObject UserPageQuery queryParams) {
        Page<UserPageVO> userPage = userService.getUserPage(queryParams);
        return PageResult.success(userPage);
    }

    @Operation(summary = "新增用户")
    @PostMapping
    @PreAuthorize("@ss.hasPerm('sys:user:add')")
    @PreventDuplicateResubmit
    public Result saveUser(@RequestBody @Valid UserForm userForm) {
        boolean result = userService.saveUser(userForm);
        return Result.judge(result);
    }

    @Operation(summary = "用户表单数据")
    @GetMapping("/{userId}/form")
    public Result<UserForm> getUserForm(@Parameter(description = "用户ID") @PathVariable Long userId) {
        UserForm userForm = userService.getUserForm(userId);
        return Result.success(userForm);
    }

    @Operation(summary = "修改用户")
    @PutMapping("/{userId}")
    @PreAuthorize("@ss.hasPerm('sys:user:edit')")
    public Result updateUser(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @RequestBody @Validated UserForm userForm) {
        boolean result = userService.updateUser(userId, userForm);
        return Result.judge(result);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{ids}")
    @PreAuthorize("@ss.hasPerm('sys:user:delete')")
    public Result deleteUsers(@Parameter(description = "用户ID，多个用英文逗号(,)分隔") @PathVariable String ids) {
        boolean result = userService.deleteUsers(ids);
        return Result.judge(result);
    }

    @Operation(summary = "修改密码")
    @PatchMapping(value = "/{userId}/password")
    @PreAuthorize("@ss.hasPerm('sys:user:reset_pwd')")
    public Result updatePassword(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @RequestBody String password) {
        boolean result = userService.updatePassword(userId, password);
        return Result.judge(result);
    }

    @Operation(summary = "修改用户状态")
    @PatchMapping(value = "/{userId}/status")
    public Result updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @Parameter(description = "状态(1:正常;0:禁用)") Integer status) {
        boolean result = userService.update(new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getId, userId)
                .set(SysUser::getStatus, status));
        return Result.judge(result);
    }

    @Operation(summary = "获取用户认证信息", hidden = true)
    @GetMapping("/{username}/authInfo")
    public Result<UserAuthInfo> getUserAuthInfo(@Parameter(description = "用户名") @PathVariable String username) {
        UserAuthInfo userAuthInfo = userService.getUserAuthInfo(username);
        return Result.success(userAuthInfo);
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/me")
    public Result<UserInfoVO> getCurrentUserInfo() {
        UserInfoVO userInfoVO = userService.getCurrentUserInfo();
        return Result.success(userInfoVO);
    }

    @Operation(summary = "退出登录")
    @DeleteMapping("/logout")
    public Result<Void> logout() {
        boolean result = userService.logout();
        return Result.judge(result);
    }

    @Operation(summary = "用户导入模板下载")
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        String fileName = "用户导入模板.xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition",
                "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

        String fileClassPath = "excel-templates" + File.separator + fileName;
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileClassPath);

        ServletOutputStream outputStream = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(outputStream).withTemplate(inputStream).build();

        excelWriter.finish();
    }

    @Operation(summary = "导入用户")
    @PostMapping("/import")
    public Result<String> importUsers(@Parameter(description = "部门ID") Long deptId, MultipartFile file) throws IOException {
        UserImportListener listener = new UserImportListener(deptId);
        EasyExcel.read(file.getInputStream(), UserImportVO.class, listener).sheet().doRead();
        String msg = listener.getMsg();
        return Result.success(msg);
    }

    @Operation(summary = "导出用户")
    @GetMapping("/export")
    public void exportUsers(UserPageQuery queryParams, HttpServletResponse response) throws IOException {
        String fileName = "用户列表.xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

        List<UserExportVO> exportUserList = userService.listExportUsers(queryParams);
        EasyExcel.write(response.getOutputStream(), UserExportVO.class).sheet("用户列表")
                .doWrite(exportUserList);
    }

    @Operation(summary = "注册用户")
    @PostMapping("/register")
    public Result<Void> registerUser(@RequestBody @Valid UserRegisterForm userRegisterForm) {
        boolean result = userService.registerUser(userRegisterForm);
        return Result.judge(result);
    }

    @Operation(summary = "发送注册短信验证码")
    @PostMapping("/register/sms_code")
    public Result<Void> sendRegistrationSmsCode(
            @Parameter(description = "手机号") @RequestParam String mobile
    ) {
        boolean result = userService.sendRegistrationSmsCode(mobile);
        return Result.judge(result);
    }

    @Operation(summary = "获取用户个人中心信息")
    @GetMapping("/profile")
    public Result<UserProfileVO> getUserProfile() {
        UserProfileVO userProfile = userService.getUserProfile();
        return Result.success(userProfile);
    }
}

