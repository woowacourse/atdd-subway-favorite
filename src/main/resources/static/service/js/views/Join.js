import {EVENT_TYPE} from '../../utils/constants.js'
import api from '../../api/index.js'

function Join() {
    const $joinButton = document.querySelector('#join-button');
    const onJoin = event => {
        event.preventDefault();
        const emailValue = document.querySelector('#email').value;
        const nameValue = document.querySelector('#name').value;
        const passwordValue = document.querySelector('#password').value;
        const passwordCheckValue = document.querySelector('#password-check').value;

        if (passwordValue !== passwordCheckValue) {
            alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return;
        }

        const data = {
            email: emailValue,
            name: nameValue,
            password: passwordValue
        };

        api.member.create(data).then((response) => {
            alert("회원가입을 축하합니다.");
            location.href = "/login";
        }).catch(() => {
        });
    };

    this.init = () => {
        $joinButton.addEventListener(EVENT_TYPE.CLICK, onJoin);
    }
}

const join = new Join();
join.init();