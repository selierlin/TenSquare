import request from '@/utils/request'
const api_group = 'user'
const api_name = 'user'
export default {
  register(user, code) {
    return request({
      url: `/${api_group}/${api_name}/register/${code}`,
      method: 'post',
      data: user
    })
  },
  sendsms(mobile) {
    return request({
      url: `/${api_group}/${api_name}/sendsms/${mobile}`,
      method: 'put'
    })
  },
  login(mobile, password) {
    return request({
      url: `/${api_group}/${api_name}/login`,
      method: 'post',
      data: {
        mobile,
        password
      }
    })
  }
}
