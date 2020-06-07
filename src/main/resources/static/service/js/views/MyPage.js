import api from '../../api/index.js'
import {myInfoTemplate} from "../../utils/templates.js";

function MyPage() {

    function initMyInfo() {
        const token = localStorage.getItem("token");
        api.member.get(token)
            .then(member => {
                document.querySelector("#member-info").innerHTML = myInfoTemplate(member);
            });
    }

    this.init = () => {
        initMyInfo()
    }
}

const myPage = new MyPage()
myPage.init()
