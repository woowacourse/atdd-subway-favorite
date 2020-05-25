import {ERROR_MESSAGE, EVENT_TYPE} from "../../utils/constants.js";

function Login() {
  const $loginButton = document.querySelector("#login-button");
  const $email = document.querySelector("#email");
  const $password = document.querySelector("#password");

  function requestLogin() {
    console.log("dkfajsklfjas")
    fetch("/login", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      }
      , body: JSON.stringify({
        email: $email.value,
        password: $password.value
      })
    }).then(data => data.json())
        .then(data => {
          localStorage.setItem("jwt", "Bearer " + data.accessToken)
          localStorage.setItem("email", data.email)
          location.href = "/";
        })
  }

  const onLogin = event => {
    event.preventDefault()
    if (!$email.value && !$password.value) {
      Snackbar.show({text: ERROR_MESSAGE.LOGIN_FAIL, pos: 'bottom-center', showAction: false, duration: 2000})
      return
    }
    requestLogin();
  }

  // const onLogin = async event => {
  //   event.preventDefault();
  //   if (!isValid()) {
  //     showSnackbar(ERROR_MESSAGE.COMMON);
  //     return;
  //   }
  //   try {
  //     const loginMember = {
  //       email: $email.value,
  //       password: $password.value
  //     };
  //     const jwt = await api.member.login(loginMember);
  //     if (jwt) {
  //       localStorage.setItem("jwt", `${jwt.tokenType} ${jwt.accessToken}`);
  //       location.href = "/";
  //       return;
  //     }
  //     showSnackbar(ERROR_MESSAGE.COMMON);
  //   } catch (e) {
  //     showSnackbar(ERROR_MESSAGE.COMMON);
  //   }
  // };
  //
  // const isValid = () => {
  //   const email = $email.value;
  //   const password = $password.value;
  //   return !email || !password;
  // };

  this.init = () => {
    $loginButton.addEventListener(EVENT_TYPE.CLICK, onLogin);
  };
}

const login = new Login();
login.init();
