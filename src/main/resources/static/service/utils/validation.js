export const isValidEmail = email => {

    const regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;

    return regExp.test(email); // 형식에 맞는 경우 true 리턴
}

export const isValidName = name => {
    const regExp = /^[^\s]+$/g;

    return regExp.test(name);
}

export const isSamePassword = (password, passwordCheck) => {
    return password === passwordCheck;
}

export const isValidPasswordLength = password => {
    return password.length > 4 && password.length < 20;
}
