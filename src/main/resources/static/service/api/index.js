const METHOD = {
    GET(token) {
        return {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            }
        }
    },
    PUT(token, data) {
    return {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify({
            ...data
        })
    }
  },
    DELETE(token) {
    return {
        method: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }
  },
  POST(data) {
    return {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        ...data
      })
    }
  }
}

const api = (() => {
  const request = (uri, config) => fetch(uri, config)
  const requestWithJsonData = (uri, config) => fetch(uri, config).then(data => data.json())

  const line = {
    getAll() {
        return request(`/admin/lines/detail`)
    },
    getAllDetail() {
        return requestWithJsonData(`/admin/lines/detail`)
    }
  }

  const path = {
      find(data) {
          return requestWithJsonData(`/paths?source=${data.source}&target=${data.target}&type=${data.type}`)
    }
  }

    const oauth = {
        login(data) {
            return requestWithJsonData(`/oauth/token`, METHOD.POST(data))
        }
    }

    const member = {
        get(token) {
            return requestWithJsonData(`/members`, METHOD.GET(token))
        },
        update(token, data) {
            return requestWithJsonData(`/members`, METHOD.PUT(token, data))
        },
        delete(token) {
            return request(`/members`, METHOD.DELETE(token))
        }
    }

  return {
    line,
      path,
      oauth,
      member
  }
})()

export default api
