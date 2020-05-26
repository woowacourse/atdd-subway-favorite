const METHOD = {
  PUT(data) {
    return {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization' : 'bearer ' + localStorage.getItem("jwt")
      },
      body: JSON.stringify({
        ...data
      })
    }
  },
  DELETE() {
    return {
      method: 'DELETE',
      headers : {
        'content-type': 'application/json',
        'Authorization' : 'bearer ' + localStorage.getItem("jwt")
      }
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
  const request = (uri, config) => fetch(uri, config)
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
    create(params){
      return request(`/members`, METHOD.POST(params));
    },
    login(params){
      return request('/oauth/token', METHOD.POST(params));
    },
    get(){
      return request('/members', {
          method : 'get',
          headers : {
              'content-type': 'application/json',
              'Authorization' : 'bearer ' + localStorage.getItem("jwt")
          }
      });
    },
    update(id, param){
      return request(`/members/${id}`, METHOD.PUT(param))
    },
    delete(id){
      return request(`members/${id}`,METHOD.DELETE())
    }
  }

  const favorite = {
    add(params){
      return request('/members/favorites', {
        method : 'post',
        headers : {
          'content-type': 'application/json',
          'Authorization' : 'bearer ' + localStorage.getItem("jwt")
        },
        body: JSON.stringify({
          ...params
        })
      });
    },
  }

  return {
    line,
    path,
    member,
    favorite
  }
})()

export default api
