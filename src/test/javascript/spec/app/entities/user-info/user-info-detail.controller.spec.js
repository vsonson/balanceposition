'use strict';

describe('Controller Tests', function() {

    describe('UserInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUserInfo, MockUser, MockNetworkMember, MockMetricHistory, MockNote, MockProgramHistory, MockUserNotification, MockWellnessHistory, MockIncentiveHistory;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUserInfo = jasmine.createSpy('MockUserInfo');
            MockUser = jasmine.createSpy('MockUser');
            MockNetworkMember = jasmine.createSpy('MockNetworkMember');
            MockMetricHistory = jasmine.createSpy('MockMetricHistory');
            MockNote = jasmine.createSpy('MockNote');
            MockProgramHistory = jasmine.createSpy('MockProgramHistory');
            MockUserNotification = jasmine.createSpy('MockUserNotification');
            MockWellnessHistory = jasmine.createSpy('MockWellnessHistory');
            MockIncentiveHistory = jasmine.createSpy('MockIncentiveHistory');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'UserInfo': MockUserInfo,
                'User': MockUser,
                'NetworkMember': MockNetworkMember,
                'MetricHistory': MockMetricHistory,
                'Note': MockNote,
                'ProgramHistory': MockProgramHistory,
                'UserNotification': MockUserNotification,
                'WellnessHistory': MockWellnessHistory,
                'IncentiveHistory': MockIncentiveHistory
            };
            createController = function() {
                $injector.get('$controller')("UserInfoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:userInfoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
