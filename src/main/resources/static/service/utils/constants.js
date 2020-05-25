export const EVENT_TYPE = {
  CLICK: 'click'
}

export const ERROR_MESSAGE = {
  LOGIN_FAIL: '😭 로그인이 실패했습니다. 다시 시도해주세요.',
  JOIN_FAIL: '😭 다시 시도해주세요.',
  JOIN_INPUT_FAIL: '😭 필드 값을 입력해주세요. 다시 시도해주세요.',
  EMAIL_FAIL: '😭 이메일형식이 아닙니다. 다시 시도해주세요.',
  EMPTY_SPACE_FAIL: '😭 공백은 입력 할 수 없습니다. 다시 시도해주세요.',
  PASSWORD_FAIL: '😭 두 패스워드가 다릅니다. 다시 시도해주세요.'
}

export const SUCCESS = {
  JOIN: '😁 회원가입 완료',
  LOGIN: '😁 로그인 완료',
  EDIT: '😁 수정 완료',
  DELETE: '😁 탈퇴 완료'
}

export const PATH_TYPE = {
  DISTANCE: 'DISTANCE',
  DURATION: 'DURATION'
}

export const REGEX = {
  EMAIL: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
  EMPTY_SPACE: /^\S*$/
}