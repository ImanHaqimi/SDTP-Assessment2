package com.photogallery.controller;

import com.photogallery.model.Member;
import com.photogallery.service.MemberService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.fetchMembers();
    }

}

