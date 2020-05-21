import api from '../../api/index.js'
import {EVENT_TYPE} from '../../utils/constants.js'

function MyPageEdit() {
    const $email = document.querySelector("#email");
    const $name = document.querySelector("#name");
    const $password = document.querySelector("#password");
    const $passwordCheck = document.querySelector("#password-check");
    const $updateButton = document.querySelector("#update-user-button");
    const $deleteButton = document.querySelector("#delete-user-button");

    const onUpdate = event => {
        event.preventDefault();
        alert("업데이트는 안돼용!!")
    }

    const onDelete = event => {
        event.preventDefault();
        if (confirm("정말 탈퇴하시겠습니까?")) {
            api.member.delete()
                .then(() => {
                    localStorage.setItem("jwt", "");
                    location.href = "/";
                })
                .catch(error => {
                    console.log(error);
                })
        }
    }

    const initField = event => {
        api.member.myinfo().then(data => {
            $email.value = data.email;
            $name.value = data.name;
        })
            .catch(error => {
                console.log(error);
            })
    }

    this.init = () => {
        initField();
        $updateButton.addEventListener(EVENT_TYPE.CLICK, onUpdate)
        $deleteButton.addEventListener(EVENT_TYPE.CLICK, onDelete)
    }
}

const myPageEdit = new MyPageEdit()
myPageEdit.init();


