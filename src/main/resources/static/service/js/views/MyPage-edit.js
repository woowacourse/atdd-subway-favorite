import {EVENT_TYPE} from '../../utils/constants.js'

function MyPageEdit() {
    const $emailValue = document.querySelector('#email');
    const $passwordValue = document.querySelector('#password');
    const $nameValue = document.querySelector('#name');
    const $updateButton = document.querySelector('#update-my-info-button');
    const $deleteButton = document.querySelector('#delete-my-info-button');


    const editButtonEventHandler = event => {
        event.preventDefault();
        fetch("/members/" + $emailValue.dataset.id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': "Bearer " + sessionStorage.getItem("token")
            },
            body: JSON.stringify({
                name: $nameValue.value,
                password: $passwordValue.value
            })
        })
    }

    const deleteButtonEventHandler = event => {
        event.preventDefault();
        fetch("/members/" + $emailValue.dataset.id, {
            method: 'DELETE',
            headers: {
                'Authorization': "Bearer " + sessionStorage.getItem("token")
            }
        })
    }

    function requestMyPageData(email, token) {
        fetch("/members?email=" + email, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': "Bearer " + token
            }
        })
            .then(data => data.json())
            .then(data => {
                    $emailValue.dataset.id = data.id;
                    $emailValue.value = data.email;
                    $nameValue.value = data.name;
                }
            )
    }

    this.loadMyPage = () => {
        const token = sessionStorage.getItem("token");
        if (token === null) {
            alert("로그인 하고 다시 오세요.");
            return;
        }
        const email = sessionStorage.getItem("email");

        requestMyPageData(email, token);
    }


    this.init = () => {
        $updateButton.addEventListener(EVENT_TYPE.CLICK, editButtonEventHandler)
        $deleteButton.addEventListener(EVENT_TYPE.CLICK, deleteButtonEventHandler)
    }
}

const myPageEdit = new MyPageEdit();
myPageEdit.loadMyPage();
myPageEdit.init();