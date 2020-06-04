import { ERROR_MESSAGE } from '../utils/constants.js'
import { request, requestWithJsonData } from './request.js'

const METHOD = {
  PUT() {
    return {
      method: 'PUT',
    }
  },
  DELETE() {
    return {
      method: 'DELETE'
    }
  },
  POST(data) {
    return {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ...data
      })
    }
  }
}

const api = (() => {
  const line = {
    getAll() {
      return request(`/lines/detail`)
    },
    getAllDetail() {
      return requestWithJsonData(`/lines/detail`)
    }
  }

  const path = {
    find(params) {
      return requestWithJsonData(`/paths?source=${params.source}&target=${params.target}&type=${params.type}`)
    }
  }

  const member = {
    join(data) {
      return request('/members', METHOD.POST(data)).catch(() => {
        throw new Error(ERROR_MESSAGE.JOIN_FAIL)
      })
    },
    login(data) {
      return requestWithJsonData('/oauth/token', METHOD.POST(data)).catch(() => {
        throw new Error(ERROR_MESSAGE.LOGIN_FAIL)
      })
    }
  }

  return {
    line,
    path,
    member
  }
})()

export default api
