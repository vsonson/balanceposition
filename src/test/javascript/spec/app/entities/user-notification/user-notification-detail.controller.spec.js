'use strict';

describe('Controller Tests', function() {

    describe('UserNotification Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUserNotification, MockUserInfo, MockAlert, MockPathWay;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUserNotification = jasmine.createSpy('MockUserNotification');
            MockUserInfo = jasmine.createSpy('MockUserInfo');
            MockAlert = jasmine.createSpy('MockAlert');
            MockPathWay = jasmine.createSpy('MockPathWay');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'UserNotification': MockUserNotification,
                'UserInfo': MockUserInfo,
                'Alert': MockAlert,
                'PathWay': MockPathWay
            };
            createController = function() {
                $injector.get('$controller')("UserNotificationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:userNotificationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
