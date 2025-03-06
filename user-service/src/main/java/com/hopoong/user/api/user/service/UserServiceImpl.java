package com.hopoong.user.api.user.service;

import com.hopoong.core.model.post.PointNotificationMessage;
import com.hopoong.core.model.post.PointUpdateMessage;
import com.hopoong.user.api.user.model.UserModel;
import com.hopoong.user.api.user.repository.UserEntityJpaRepository;
import com.hopoong.user.domain.UserEntity;
import com.hopoong.user.event.UserEventHandler;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {


    private final UserEntityJpaRepository userEntityJpaRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final UserEventHandler userEventHandler;
    private final UserService self;

    @Autowired
    public UserServiceImpl(
            UserEntityJpaRepository userEntityJpaRepository,
            ApplicationEventPublisher eventPublisher,
            UserEventHandler userEventHandler,
                       @Lazy UserService self) {
        this.userEntityJpaRepository = userEntityJpaRepository;
        this.eventPublisher = eventPublisher;
        this.userEventHandler = userEventHandler;
        this.self = self;
    }


    /*
     * 특정 사용자 정보 조회
     */
    @Transactional(readOnly = true)
    public UserEntity getUserById(Long userId) {
        return userEntityJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }

    /*
     * 사용자 포인트 추가
     */
    @Transactional
    public UserEntity addUserPoints(Long userId, UserModel.UserPointUpdateRequest request) {
        UserEntity userEntity = getUserByIdWithLock(userId);
        userEntity.setPoint(userEntity.getPoint() + request.point());
        userEntityJpaRepository.save(userEntity);
        return userEntity;
    }

    /*
     * 사용자 포인트 추가 & 알람 설정
     */
    @Transactional
    public void updatePointWithNotification(PointUpdateMessage pointUpdateMessage) {
        UserEntity userEntity = self.addUserPoints(pointUpdateMessage.userId(), new UserModel.UserPointUpdateRequest(pointUpdateMessage.point()));
        PointNotificationMessage pointNotificationMessage = new PointNotificationMessage(pointUpdateMessage.userId(), pointUpdateMessage.postId(), pointUpdateMessage.point(), userEntity.getPoint(), LocalDateTime.now(), "post_created");
        eventPublisher.publishEvent(pointNotificationMessage);
    }


    @Override
    @Transactional
    public void signup(UserModel.UserSignupRequest request) {

        // 이메일 중복 확인
        if (userEntityJpaRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = "sdlmwwl23sm21sd2@31ma29@387@Ssf&#*Aa*s@d%wWDsdasdwW";

        // 사용자 저장
        UserEntity user = UserEntity.builder()
                .userName(request.userName())
                .email(request.email())
                .password(encodedPassword)
                .point(0L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userEntityJpaRepository.save(user);
    }



    /*
     * 비관적 락
     */
    @Transactional(readOnly = true)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public UserEntity getUserByIdWithLock(Long userId) {
        return userEntityJpaRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }
}
