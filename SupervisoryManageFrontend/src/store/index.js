import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    apiId: 0,
    modelName:"",
    apiKey: ""


  },
  mutations: {
    setApiId(state, apiId){
      state.apiId = apiId;
    },

    setModelName(state, modelName){
      state.modelName = modelName
    },

    setApiKey(state, apiKey){
      state.apiKey = apiKey
    }


  },
  actions: {
  },
  modules: {
  }
})
