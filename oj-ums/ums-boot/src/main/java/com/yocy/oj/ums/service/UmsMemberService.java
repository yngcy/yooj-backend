package com.yocy.oj.ums.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yocy.oj.ums.dto.MemberAddressDTO;
import com.yocy.oj.ums.dto.MemberAuthDTO;
import com.yocy.oj.ums.dto.MemberRegisterDTO;
import com.yocy.oj.ums.model.entity.UmsMember;
import com.yocy.oj.ums.model.vo.MemberVO;

import java.util.List;

/**
* @author 25055
* @description 针对表【ums_member(会员表)】的数据库操作Service
* @createDate 2024-04-28 10:07:00
*/
public interface UmsMemberService extends IService<UmsMember> {

    /**
     * 获取会员列表（管理员）
     * @param page
     * @param nickname
     * @return
     */
    Page<UmsMember> list(Page<UmsMember> page, String nickname);

    /**
     * 根据 openid 获取会员认证信息
     * @param openid
     * @return
     */
    MemberAuthDTO getMemberByOpenid(String openid);

    /**
     * 根据手机号获取会员认证信息
     * @param mobile
     * @return
     */
    MemberAuthDTO getMemberByMobile(String mobile);

    /**
     * 新增会员
     * @param memberRegisterDTO
     * @return
     */
    Long addMember(MemberRegisterDTO memberRegisterDTO);

    /**
     * 获取登录会员信息
     * @return
     */
    MemberVO getCurrentMemberInfo();

    /**
     * 获取会员地址列表
     * @param memberId
     * @return
     */
    List<MemberAddressDTO> listMemberAddress(Long memberId);
}
