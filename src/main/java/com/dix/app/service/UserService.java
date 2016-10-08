package com.dix.app.service;

import com.dix.app.exception.*;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.dix.base.common.*;
import com.dix.base.exception.*;
import com.dix.app.mapper.UserMapper;
import com.dix.app.mapper.UserBindMapper;
import com.dix.app.model.User;
import com.dix.app.model.UserBind;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by dd on 1/26/16.
 */
@Component
public class UserService {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserMapper userMapper;

    private final UserBindMapper userBindMapper;

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public UserService(UserBindMapper userBindMapper, @Qualifier("RedisTemplate") RedisTemplate<String, String> redisTemplate, UserMapper userMapper) {
        this.userBindMapper = userBindMapper;
        this.redisTemplate = redisTemplate;
        this.userMapper = userMapper;
    }

    public User loginByPhone(String phone, String password) throws InvalidKeySpecException, NoSuchAlgorithmException, LoginFailException {
        User user = userMapper.existsByPhone(phone);
        if (user == null) {
            throw new NotExistsException();
        }

        if (Strings.isNullOrEmpty(user.getPassword()))
        {
            throw new LoginFailException();
        }

        if (!PasswordHash.validatePassword(password, user.getPassword())) {
            throw new LoginFailException();
        }

        return user;
    }

    @Transactional
    public User register(String phone, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        User user = userMapper.existsByPhone(phone);
        if (user != null) {
            throw new ExistsException();
        }

        user = new User();
        user.setPhone(phone);
        user.setPassword(PasswordHash.createHash(password));
        user.setCreateTime(Util.time());
        user.setUpdateTime(user.getCreateTime());

        logger.trace(String.format("user password: %s", user.getPassword()));

        return registerUserWithUid(user);

        // throw new RegisterFailException("can not reach here");
    }

    @Transactional
    public User registerUserWithUid(User user) {
        String uid;
        int tryCount = 0;
        while (true) {
            int tryGenerateUidCount = 0;
            // generate an available uid
            while (true) {
                uid = RandomStringUtils.randomAlphanumeric(9);

                User userEntityExists = userMapper.existsByUidIgnoreCase(uid.toLowerCase());
                if (userEntityExists == null) {
                    break;
                }

                tryGenerateUidCount++;
                if (tryGenerateUidCount >= 3) {
                    throw new RegisterFailException("fail too many times when generate uid");
                }
            }

            // uid = RandomStringUtils.randomAlphanumeric(9);

            // lock uid
            String keyLock = Const.getKeyOfUserUidLock(uid);
            Long lock = redisTemplate.boundValueOps(keyLock).increment(1);
            redisTemplate.boundValueOps(keyLock).expire(60, TimeUnit.SECONDS);
            logger.trace(String.format("key: %s, lock: %d", keyLock, lock));
            if (lock == 1) {
                user.setUid(uid);
                userMapper.insert(user);
                return user;
            }

            tryCount++;
            if (tryCount >= 3) {
                throw new RegisterFailException("fail too many times");
            }
        }
    }

    public User findByPhone(String phone) throws NotExistsException {
        User user = userMapper.existsByPhone(phone);
        if (user == null) {
            throw new NotExistsException();
        }

        return user;
    }

    public User findByUid(String uid) throws NotExistsException {
        User user = userMapper.getUserByUid(uid);
        if (user == null) {
            throw new NotExistsException();
        }

        return user;
    }

    public User findById(Long id) throws NotExistsException {
        User user = userMapper.existsById(id);
        if (user == null) {
            throw new NotExistsException();
        }

        return user;
    }

    public void updatePassword(User user, String oldPassword, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (!PasswordHash.validatePassword(oldPassword, user.getPassword())) {
            throw new LoginFailException("wrong password");
        }

        user.setPassword(PasswordHash.createHash(password));

        userMapper.update(user);
    }

    public void resetPassword(User user, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        user.setPassword(PasswordHash.createHash(password));
        userMapper.update(user);
    }

    public void resetPasswordByPhone(String phone, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        User user = userMapper.existsByPhone(phone);

        if (user == null) {
            throw new UserNotExistsException();
        }

        user.setPassword(PasswordHash.createHash(password));

        userMapper.update(user);
    }

    public void updatePhone(User user, String phone)
    {
        User userExists = userMapper.existsByPhone(phone);
        if (userExists != null) {
            throw new PhoneHasBeenTakenException();
        }

        user.setPhone(phone);

        userMapper.update(user);
    }

    public void updateAvatar(User user, String avatar) {
        user.setAvatar(avatar);
        userMapper.update(user);
    }

    public User loginByOuterAccount(Integer type, String outerUserId) {
        if (!Lists.newArrayList(UserBind.TYPE_WEXIN).contains(type)) {
            throw new WrongParamException("type");
        }

        User user = userMapper.getUserByOuterUserId(type, outerUserId);
        if (user == null) {
            throw new LoginFailException("user bind not exists");
        }

        return user;
    }

    @Transactional
    public User registerByOuterAccount(Integer type, String outerUserId) {
        if (!Lists.newArrayList(UserBind.TYPE_WEXIN).contains(type)) {
            throw new WrongParamException("type");
        }

        User user = userMapper.getUserByOuterUserId(type, outerUserId);
        if (user != null) {
            throw new ExistsException();
        }

        user = new User();
        user.setCreateTime(Util.time());
        user.setUpdateTime(user.getCreateTime());
        user = registerUserWithUid(user);

        UserBind userBind = new UserBind();
        userBind.setUserId(user.getId());
        userBind.setType(type);
        userBind.setOuterUserId(outerUserId);
        userBind.setWeight(Const.WEIGHT_NORMAL);
        userBind.setCreateTime(Util.time());
        userBind.setUpdateTime(userBind.getCreateTime());
        userBind.save();

        return user;
    }

    public void updateOuterUserData(Integer type, User user, String data) {
        if (!Lists.newArrayList(UserBind.TYPE_WEXIN).contains(type)) {
            throw new WrongParamException("type");
        }

        UserBind userBind = userBindMapper.existsBindByUser(UserBind.TYPE_WEXIN, user.getId());

        if (userBind == null) {
            throw new NotExistsException("user bind not exist");
        }

        userBind.setData(data);
        userBindMapper.update(userBind);
    }

    public void updateOuterUserDataByOuterUserId(Integer type, String outerUserId, String data) {
        if (!Lists.newArrayList(UserBind.TYPE_WEXIN).contains(type)) {
            throw new WrongParamException("type");
        }

        UserBind userBind = userBindMapper.existsBindByOuterUser(UserBind.TYPE_WEXIN, outerUserId);

        if (userBind == null) {
            throw new NotExistsException("user bind not exist");
        }

        userBind.setData(data);
        userBindMapper.update(userBind);
    }

    public List getUserBindList(User user) throws NotExistsException {
        List<Map> userBinds = userBindMapper.getUserBindList(user.getId());
        return Core.processModelList(userBinds, UserBind.class);
    }

    public List getUserList(Integer page) throws NotExistsException {
        page = page < 1 ? 1 : page;
        Integer limit = Const.PAGE_LIMIT;
        Integer offset = (page - 1) * limit;

        List<Map> users = userMapper.getUserList(limit, offset);
        return Core.processModelList(users, User.class);
    }

    public Integer getUserCount() {
        return userMapper.getUserCount();
    }


    public void bindOuterUser(Integer type, User user, String outerUserId, String data)
    {
        bindOuterUser(user, type, outerUserId, data);
    }

    private void bindOuterUser(User user, Integer type, String outerUserId, String data) {

        if (!Lists.newArrayList(UserBind.TYPE_WEXIN).contains(type)) {
            throw new WrongParamException("type");
        }

        UserBind userBind = userBindMapper.existsBindByOuterUser(type, outerUserId);
        if (userBind != null) {
            if (!userBind.getUserId().equals(user.getId())) {
                throw new BindOuterUserFailOuterUserHasBeenBindToOtherUserException();
            } else {
                return;
            }
        }

        userBind = userBindMapper.existsBindByUser(type, user.getId());
        if (userBind != null) {
            if (!userBind.getOuterUserId().equals(outerUserId)) {
                throw new BindOuterUserFailUserHasBeenBondByOtherOuterUserException();
            } else {
                return;
            }

        }

        userBind = new UserBind();
        userBind.setUserId(user.getId());
        userBind.setType(type);
        userBind.setOuterUserId(outerUserId);
        userBind.setData(data);
        userBind.setWeight(Const.WEIGHT_NORMAL);
        userBind.setCreateTime(Util.time());
        userBind.setUpdateTime(userBind.getCreateTime());
        userBind.save();
    }

    public void unbindOuterUser(Integer type, User user) {
        if (!Lists.newArrayList(UserBind.TYPE_WEXIN).contains(type)) {
            throw new WrongParamException("type");
        }

        userBindMapper.deleteUserBindOfType(type, user.getId());
    }

    public void updateBasicInfo(User user, String name, String email, String tel, Integer gender, Long birthday) {

        if (name != null && StringUtils.isNotBlank(name)) {
            user.setName(name);
        }
        if (email != null && StringUtils.isNotBlank(email)) {
            user.setEmail(email);
        }
        if (tel != null && StringUtils.isNotBlank(tel)) {
            user.setTel(tel);
        }
        if (gender >= 0) {
            user.setGender(gender);
        }
        if (birthday > 0L) {
            user.setBirthday(birthday);
        }
        userMapper.update(user);
    }

    public User checkUserByUid(String uid) {

        User user = userMapper.getUserByUid(uid);
        if (user == null) {
            throw new NotExistsException();
        }

        return user;
    }

    public User checkUserByUidOrUserId(String uid, Long userId)
    {
        if (uid == null && userId == null)
        {
            throw new ParamNotSetException("uid / user_id");
        }

        User user = null;
        if (uid != null && StringUtils.isNotEmpty(uid))
        {
            user = checkUserByUid(uid);
        }
        if (userId != null) {
            user = checkUserById(userId);
        }

        return user;
    }

    public User checkUserById(Long id) {

        User user = userMapper.existsById(id);
        if (user == null) {
            throw new NotExistsException();
        }

        return user;
    }

    public Map<String, Object> getRawById(Long id) {
        User user = userMapper.findById(id);

        if (user == null)
        {
            return null;
        }
        return user.processModel();
    }

    @Transactional
    public void testTransaction()
    {
        User user = new User();
        user.setUid("-x");
        user.setUsername("dd");
        user.save();

        // throw new NotAllowedException();
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }

}
