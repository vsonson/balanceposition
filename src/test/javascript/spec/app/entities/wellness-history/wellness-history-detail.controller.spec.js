'use strict';

describe('Controller Tests', function() {

    describe('WellnessHistory Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockWellnessHistory, MockUserInfo, MockWellnessItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockWellnessHistory = jasmine.createSpy('MockWellnessHistory');
            MockUserInfo = jasmine.createSpy('MockUserInfo');
            MockWellnessItem = jasmine.createSpy('MockWellnessItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'WellnessHistory': MockWellnessHistory,
                'UserInfo': MockUserInfo,
                'WellnessItem': MockWellnessItem
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
