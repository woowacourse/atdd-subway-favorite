export const EVENT_TYPE = {
  CLICK: 'click'
}

const ERROR = {
  COMMON: { code: 0, message: '🕵️‍♂️ 에러가 발생했습니다.' },
  LOGIN_FAIL: { code: 100, message: '😭 로그인이 실패했습니다. 다시 시도해주세요.' },
  NOT_MATCH_PASSWORD: { code: 200, message: '😭 패스워드가 일치하지 않습니다.' },
  DUPLICATED_EMAIL: { code: 1000, message: '😭 이메일이 중복되었습니다. 다른 이메일을 입력해주세요.' },
  MEMBER_UNAUTHORIZED: { code: 2000, message: '😭 허용되지 않은 접근입니다.' },
}

export const ERROR_MESSAGE = (code) => {
  let error = Object.values(ERROR).find(error => error.code === code);
  if (!error) {
    error = ERROR.COMMON
  }
  return error.message;
}

export const SUCCESS_MESSAGE = {
  COMMON: "😁 성공적으로 변경되었습니다.",
  SAVE: "😁 정보가 반영되었습니다..",
  FAVORITE: "😁 즐겨찾기에 추가하였습니다."
};


export const PATH_TYPE = {
  DISTANCE: "DISTANCE",
  DURATION: "DURATION"
};
