'use strict';

describe('Controller Tests', function() {

    describe('QuoteOfTheDay Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockQuoteOfTheDay, MockUserInfo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockQuoteOfTheDay = jasmine.createSpy('MockQuoteOfTheDay');
            MockUserInfo = jasmine.createSpy('MockUserInfo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'QuoteOfTheDay': MockQuoteOfTheDay,
                'UserInfo': MockUserInfo
            };
            createController = function() {
                $injector.get('$controller')("QuoteOfTheDayDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'balancepositionApp:quoteOfTheDayUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
