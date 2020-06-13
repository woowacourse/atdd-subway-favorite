import api from "../../api/index.js";

function MyPage() {
    const $memberEmail = document.querySelector('#member-email');
    const $memberName = document.querySelector('#member-name');

    const initMemberDetail = async () => {
        const memberDetail = await api.loginMember.get().catch(() => {
            alert("로그인 해주세요.");
            return location.href = "/login";
            }
        );

        $memberEmail.innerText = memberDetail.email;
        $memberName.innerText = memberDetail.name;
    }

    this.init = () => {
        initMemberDetail();
    };
}

const myPage = new MyPage();

myPage.init();