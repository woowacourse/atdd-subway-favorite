import api from '../../api/index.js'
import {EVENT_TYPE} from '../../utils/constants.js'

function MyPageEdit() {
    const email = document.querySelector('#email');
    const name = document.querySelector('#name');
    const password = document.querySelector('#password');
    const $saveButton = document.querySelector("#save-button");
    const $deleteButton = document.querySelector("#delete-button");

    const onSave = (event) => {
        event.preventDefault();
        const data = {
            name: name.value,
            password: password.value
        }
        api.member.update(data).then(() => {
            alert("수정되었습니다.");
            location.href = "/mypage";
        });
    }

    const onDelete = (event) => {
        event.preventDefault();
        api.member.delete().then(() => {
            alert("탈퇴되었습니다.");
            location.href = "/";
        });
    }

    this.init = () => {
        api.member.get().then(async (response) => {
            const memberResponse = await response.json();
            email.value = memberResponse.email;
            name.value = memberResponse.name;
        });

        $saveButton.addEventListener(EVENT_TYPE.CLICK, onSave)
        $deleteButton.addEventListener(EVENT_TYPE.CLICK, onDelete)
    }
}

const myPageEdit = new MyPageEdit();
myPageEdit.init();
