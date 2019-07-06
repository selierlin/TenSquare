import Cookies from 'js-cookie'
const TokenKey = 'User-Token'
const NameKey = 'User-Name'
const AvatarKey = 'User-Avatar'
/*export default {
  setUser(token, name, avatar) {
    Cookies.set(NameKey, name)
    Cookies.set(AvatarKey, avatar)
    Cookies.set(TokenKey, token)
  }
}*/
export function setUser(token, name, avatar) {
  Cookies.set(NameKey, name)
  Cookies.set(AvatarKey, avatar)
  Cookies.set(TokenKey, token)
}
export function getUser() {
  return {
    token: Cookies.get(TokenKey),
    name: Cookies.get(NameKey),
    avatar: Cookies.get(AvatarKey)
  }
}
export function removeUser() {
  Cookies.remove(TokenKey)
  Cookies.remove(NameKey)
  Cookies.remove(AvatarKey)
}
