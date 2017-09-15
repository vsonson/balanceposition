'use strict';

describe('Controller Tests', function() {

    describe('WellnessHistory Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockWellnessHistory, MockWellnessItem, MockUserInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockWellnessHistory = jasmine.createSpy('MockWellnessHistory');
            MockWellnessItem = jasmine.createSpy('MockWellnessItem');
            MockUserInfo = jasmine.createSpy('MockUserInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'WellnessHistory': MockWellnessHistory,
                'WellnessItem': MockWellnessItem,
                'UserInfo': MockUserInfo
            };
            createController = function() {
                $injector.get('$controller')("WellnessHistoryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:wellnessHistoryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
