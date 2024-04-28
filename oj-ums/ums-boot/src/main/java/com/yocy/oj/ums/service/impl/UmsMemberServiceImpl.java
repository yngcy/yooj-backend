package com.yocy.oj.ums.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yocy.common.result.ResultCode;
import com.yocy.common.security.utils.SecurityUtils;
import com.yocy.common.web.exception.BizException;
import com.yocy.oj.ums.converter.AddressConverter;
import com.yocy.oj.ums.converter.MemberConverter;
import com.yocy.oj.ums.dto.MemberAddressDTO;
import com.yocy.oj.ums.dto.MemberAuthDTO;
import com.yocy.oj.ums.dto.MemberRegisterDTO;
import com.yocy.oj.ums.mapper.UmsMemberMapper;
import com.yocy.oj.ums.model.entity.UmsAddress;
import com.yocy.oj.ums.model.entity.UmsMember;
import com.yocy.oj.ums.model.vo.MemberVO;
import com.yocy.oj.ums.service.UmsAddressService;
import com.yocy.oj.ums.service.UmsMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 25055
* @description 针对表【ums_member(会员表)】的数据库操作Service实现
* @createDate 2024-04-28 10:07:00
*/
@Service
@RequiredArgsConstructor
@Slf4j
public class UmsMemberServiceImpl extends ServiceImpl<UmsMemberMapper, UmsMember>
    implements UmsMemberService{

    private final RedisTemplate redisTemplate;

    private final MemberConverter memberConverter;

    private final AddressConverter addressConverter;

    private final UmsAddressService addressService;


    @Override
    public Page<UmsMember> list(Page<UmsMember> page, String nickname) {
        List<UmsMember> memberList = this.baseMapper.list(page, nickname);
        page.setRecords(memberList);
        return page;
    }

    @Override
    public MemberAuthDTO getMemberByOpenid(String openid) {
        UmsMember entity = this.getOne(new LambdaQueryWrapper<UmsMember>()
                .eq(UmsMember::getOpenid, openid)
                .select(UmsMember::getId,
                        UmsMember::getOpenid,
                        UmsMember::getStatus));
        if (entity == null) {
            throw new BizException(ResultCode.USER_NOT_EXIST);
        }

        return memberConverter.entity2OpenidAuthDTO(entity);
    }

    @Override
    public MemberAuthDTO getMemberByMobile(String mobile) {
        UmsMember entity = this.getOne(new LambdaQueryWrapper<UmsMember>()
                .eq(UmsMember::getMobile, mobile)
                .select(UmsMember::getId,
                        UmsMember::getMobile,
                        UmsMember::getStatus));
        if (entity == null) {
            throw new BizException(ResultCode.USER_NOT_EXIST);
        }

        return memberConverter.entity2MobileAuthDTO(entity);
    }

    @Override
    public Long addMember(MemberRegisterDTO memberRegisterDTO) {
        UmsMember umsMember = memberConverter.dto2Entity(memberRegisterDTO);
        boolean result = this.save(umsMember);
        Assert.isTrue(result, "新增会员失败");
        return umsMember.getId();
    }

    @Override
    public MemberVO getCurrentMemberInfo() {
        Long memberId = SecurityUtils.getMemberId();

        UmsMember umsMember = this.getOne(new LambdaQueryWrapper<UmsMember>()
                .eq(UmsMember::getId, memberId)
                .select(UmsMember::getId,
                        UmsMember::getNickName,
                        UmsMember::getAvatarUrl,
                        UmsMember::getMobile,
                        UmsMember::getBalance));

        MemberVO memberVO = new MemberVO();
        BeanUtil.copyProperties(umsMember, memberVO);
        return memberVO;
    }

    @Override
    public List<MemberAddressDTO> listMemberAddress(Long memberId) {

        List<UmsAddress> addressList = addressService.list(new LambdaQueryWrapper<UmsAddress>()
                .eq(UmsAddress::getMemberId, memberId));

        List<MemberAddressDTO> result = addressConverter.entity2DTO(addressList);
        return result;
    }
}




