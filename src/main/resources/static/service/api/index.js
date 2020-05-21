const METHOD = {
    GET_WITH_AUTH(token) {
        return {
            method: 'GET',
            headers: {
                'Authorization': token,
            }
        }
    },
    PUT(token, data) {
        return {
            method: 'PUT',
            headers: {
                'Authorization': token,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                ...data
            })
        }
    },
    DELETE(id, token) {
        return {
            method: 'DELETE',
            headers: {
                'Authorization': token,
             },
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
        create(data) {
            return request(`/members`, METHOD.POST(data))
        },
        login(data) {
            return request(`/login`, METHOD.POST(data))
        },
        getWithAuth(token) {
            return requestWithJsonData(`/members`, METHOD.GET_WITH_AUTH(token))
        },
        update(id, token, data) {
            return request(`/members/${id}`, METHOD.PUT(token, data))
        },
        delete(id, token) {
            return request(`/members/${id}`, METHOD.DELETE(id, token))
        }
    }

    return {
        line,
        path,
        member
    }
})()

export default api
