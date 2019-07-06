import request from '@/utils/request'
const api_group = 'qa'
const api_name = 'problem'
export default {
  list(type, label, page, size) {
    return request({
      url: `/${api_group}/${api_name}/${type}/${label}/${page}/${size}`,
      method: 'get'
    })
  },
  findById(id) {
    return request({
      url: `/${api_group}/${api_name}/${id}`,
      method: 'get'
    })
  },
  commentlist(id) {
    return request({
      url: `/${api_group}/${api_name}/commentlist/${id}`,
      method: 'get'
    })
  },
  save(pojo) {
    return request({
      url: `/${api_group}/${api_name}`,
      method: 'post',
      data: pojo
    })
  }
}
