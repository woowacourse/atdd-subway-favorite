const METHOD = {
  GET_WITH_AUTH() {
    return {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("jwt") || ""
      }
    };
  },
  PUT(data) {
    return {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("jwt") || ""
      },
      body: JSON.stringify({
        ...data
      })
    };
  },
  DELETE() {
    return {
      method: "DELETE",
      headers: {
        Authorization: localStorage.getItem("jwt") || ""
      }
    };
  },

  DELETE_FAVORITE() {
    return {
      method: "DELETE",
      headers: {
        Authorization: localStorage.getItem("jwt") || ""
      }
    };
  },

  POST(data) {
    return {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("jwt") || ""
      },
      body: JSON.stringify({
        ...data
      })
    };
  }
};

const api = (() => {
  const request = (uri, config) => fetch(uri, config);
  const requestWithJsonData = (uri, config) =>
    fetch(uri, config).then(response => {
      if (!response.ok) {
        throw new Error();
      }
      return response.json();
    });

  const path = {
    find(params) {
      return requestWithJsonData(`/paths?source=${params.source}&target=${params.target}&type=${params.type}`)
    }
  }
  const member = {
    get(id) {
      return requestWithJsonData(`/members/${id}`);
    },
    create(newMember) {
      return request(`/members`, METHOD.POST(newMember));
    },
    update(id, updatedData) {
      return request(`/members/${id}`, METHOD.PUT(updatedData));
    },
    delete(id) {
      return request(`/members/${id}`, METHOD.DELETE());
    },
    login(loginInfo) {
      return requestWithJsonData(`/oauth/token`, METHOD.POST(loginInfo));
    }
  };

  const favorite = {
    get() {
      return requestWithJsonData(`/favorites`, METHOD.GET_WITH_AUTH());
    },
    create(favoriteInput) {
      return requestWithJsonData(`/favorites`, METHOD.POST(favoriteInput));
    },
    delete(favoriteInput) {
      return request(`/favorites?source=${favoriteInput.source}&target=${favoriteInput.target}`, METHOD.DELETE_FAVORITE(favoriteInput));
    }
  }

  const loginMember = {
    get() {
      return requestWithJsonData(`/me/bearer`, METHOD.GET_WITH_AUTH());
    },
    update(updatedInfo) {
      return request(`/me`, METHOD.PUT(updatedInfo));
    },
    delete() {
      return request(`/me`, METHOD.DELETE());
    }
  };

  return {
    member,
    favorite,
    loginMember,
    path
  };
})();

export default api;
