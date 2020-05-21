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

const api = (() => {
    const request = (uri, config) => fetch(uri, config)
    const requestWithJsonData = (uri, config) => fetch(uri, config).then(data => data.json())

    const member = {
        create(data) {
            return request("/members", METHOD.POST(data))
        },
    }
    const loginMember = {
        login(data) {
            return requestWithJsonData("/oauth/token", METHOD.POST(data));
        },
    }

    return {
        member,
        loginMember
    }
})()

export default api
