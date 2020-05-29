import api from '../../api/index.js'

function MyPage() {
    this.init = () => {
        api.member.get().then(async (response) => {
            const memberResponse = await response.json();
            const email = document.querySelector('#email');
            const name = document.querySelector('#name');
            email.innerText = memberResponse.email;
            name.innerText = memberResponse.name;
        }).catch(() => {
        });
    }
}

const myPage = new MyPage();
myPage.init();
