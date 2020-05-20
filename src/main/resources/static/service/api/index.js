const METHOD = {
  PUT() {
    return {
      method: 'PUT'
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

const resolver = (response) => {
  return new Promise((resolve, reject) => {
    let func;
    response.status < 400 ? func = resolve : func = reject;
    if (response.status !== 204) {
      response.json().then(data => func({'status': response.status, 'body': data}));
      return;
    }
    func({'status': response.status})
  });
}

const api = (() => {
  const request = (uri, config) => fetch(uri, config).then(resolver);
  const requestWithJsonData = (uri, config) => fetch(uri, config).then(data => data.json())

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
      return request("/members", METHOD.POST(data));
    }
  }

  return {
    line,
    path,
    member
  }
})()

export default api
