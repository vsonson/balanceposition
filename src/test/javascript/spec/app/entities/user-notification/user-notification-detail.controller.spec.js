'use strict';

describe('Controller Tests', function() {

    describe('UserNotification Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUserNotification, MockPathWay, MockAlert, MockUserInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUserNotification = jasmine.createSpy('MockUserNotification');
            MockPathWay = jasmine.createSpy('MockPathWay');
            MockAlert = jasmine.createSpy('MockAlert');
            MockUserInfo = jasmine.createSpy('MockUserInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'UserNotification': MockUserNotification,
                'PathWay': MockPathWay,
                'Alert': MockAlert,
                'UserInfo': MockUserInfo
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
