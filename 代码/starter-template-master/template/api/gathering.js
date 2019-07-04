import request from '@/utils/request'
const api_name = 'gathering/gathering'
export default {
getList() { return request({ url: `/${api_name}`, method: 'get' }) },
getPageList(page,size) { return request({ url: `/${api_name}/search/${page}/${size}`, method: 'get' }) },
search(page,size,searchMap) { return request({ url: `/${api_name}/search/${page}/${size}`, method: 'post', data: searchMap }) },
save(pojo) { return request({ url: `/${api_name}`, method: 'post', data: pojo }) },
findById(id) { return request({ url: `/${api_name}/${id}`, method: 'get' }) },
update(id,pojo) { return request({ url: `/${api_name}/${id}`, method: 'put', data: pojo }) },
deleteById(id) { return request({ url: `/${api_name}/${id}`, method: 'delete' }) }
}
