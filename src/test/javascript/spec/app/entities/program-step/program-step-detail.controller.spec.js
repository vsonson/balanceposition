'use strict';

describe('Controller Tests', function() {

    describe('ProgramStep Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProgramStep, MockProgramLevel, MockProgramHistory;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProgramStep = jasmine.createSpy('MockProgramStep');
            MockProgramLevel = jasmine.createSpy('MockProgramLevel');
            MockProgramHistory = jasmine.createSpy('MockProgramHistory');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ProgramStep': MockProgramStep,
                'ProgramLevel': MockProgramLevel,
                'ProgramHistory': MockProgramHistory
            };
            createController = function() {
                $injector.get('$controller')("ProgramStepDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:programStepUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
