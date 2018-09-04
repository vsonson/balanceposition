'use strict';

describe('Controller Tests', function() {

    describe('UserInfo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUserInfo, MockUser, MockNetworkMember, MockMetricHistory, MockProgramHistory, MockUserNotification, MockWellnessHistory, MockIncentiveHistory, MockQuoteOfTheDay;
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
            MockProgramHistory = jasmine.createSpy('MockProgramHistory');
            MockUserNotification = jasmine.createSpy('MockUserNotification');
            MockWellnessHistory = jasmine.createSpy('MockWellnessHistory');
            MockIncentiveHistory = jasmine.createSpy('MockIncentiveHistory');
            MockQuoteOfTheDay = jasmine.createSpy('MockQuoteOfTheDay');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'UserInfo': MockUserInfo,
                'User': MockUser,
                'NetworkMember': MockNetworkMember,
                'MetricHistory': MockMetricHistory,
                'ProgramHistory': MockProgramHistory,
                'UserNotification': MockUserNotification,
                'WellnessHistory': MockWellnessHistory,
                'IncentiveHistory': MockIncentiveHistory,
                'QuoteOfTheDay': MockQuoteOfTheDay
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
