'use strict';

describe('Controller Tests', function() {

    describe('PathHistory Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPathHistory, MockPathWay, MockPathStep, MockPathAction;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPathHistory = jasmine.createSpy('MockPathHistory');
            MockPathWay = jasmine.createSpy('MockPathWay');
            MockPathStep = jasmine.createSpy('MockPathStep');
            MockPathAction = jasmine.createSpy('MockPathAction');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PathHistory': MockPathHistory,
                'PathWay': MockPathWay,
                'PathStep': MockPathStep,
                'PathAction': MockPathAction
            };
            createController = function() {
                $injector.get('$controller')("PathHistoryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:pathHistoryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
