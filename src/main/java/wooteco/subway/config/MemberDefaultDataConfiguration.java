package wooteco.subway.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;

@Configuration
public class MemberDefaultDataConfiguration {
    @Configuration
    @Profile("local")
    private class LocalDataApplicationRunner implements ApplicationRunner {
        private final MemberService memberService;

        public LocalDataApplicationRunner(MemberService memberService) {
            this.memberService = memberService;
        }

        @Override
        public void run(ApplicationArguments args) throws Exception {
            Member member = new Member("ramen6315@gmail.com", "ramen", "6315");
            memberService.createMember(member);
        }
    }
}
