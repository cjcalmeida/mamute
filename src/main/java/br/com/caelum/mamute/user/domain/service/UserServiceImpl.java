package br.com.caelum.mamute.user.domain.service;

import br.com.caelum.mamute.infrastructure.log.LogException;
import br.com.caelum.mamute.user.domain.LoginMethod;
import br.com.caelum.mamute.user.domain.SearchCriteria;
import br.com.caelum.mamute.user.domain.UserEntity;
import br.com.caelum.mamute.user.domain.repository.LoginMethodRepository;
import br.com.caelum.mamute.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional(propagation = Propagation.REQUIRED)
class UserServiceImpl implements UserService {

    private static final int FIRST_INDEX = 0;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginMethodRepository loginMethodRepository;

    @LogException(exceptions = { Exception.class })
    public UserEntity signup(final UserEntity user) {
        this.validate(user);
        final UserEntity userPersisted = this.userRepository.save(user);
        final LoginMethod loginMethodCreated = this.loginMethodRepository
                .save(userPersisted.getLoginMethods().get(FIRST_INDEX));

        return userPersisted;
    }

    @Override
    @LogException(exceptions = { Exception.class })
    public Page<UserEntity> findUserByFilter(SearchCriteria criteria, Pageable pageable) {

        final PageRequest page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC);

        return this.userRepository.findAll(criteria.toSpecification(), page);
    }

    private void validate(final UserEntity user) {
        Assert.noNullElements(user.getLoginMethods(), "Login method is blank");
        Assert.notEmpty(user.getLoginMethods(), "Login method is blank");
        final UserEntity duplicateUserForEmail = this.userRepository.findUserByEmail(user.getEmail());
        Assert.isNull(duplicateUserForEmail, "Email Address is Already Registered");
    }
}
