import api from '../../api/index.js'

function ValidateLogin() {
  const validate = () => {
    api.member.find()
      .then(data => {
        if (data.status === 401) {
          alert("로그인 해주세요")
          location.href = '/'
        }
      })
  }

  this.init = () => {
    validate()
  }
}

const validateLogin = new ValidateLogin()
validateLogin.init()
