(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('QuoteOfTheDayHistoryDialogController', QuoteOfTheDayHistoryDialogController);

    QuoteOfTheDayHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'QuoteOfTheDayHistory', 'UserInfo', 'QuoteOfTheDay'];

    function QuoteOfTheDayHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, QuoteOfTheDayHistory, UserInfo, QuoteOfTheDay) {
        var vm = this;

        vm.quoteOfTheDayHistory = entity;
        vm.clear = clear;
        vm.save = save;
        vm.userinfos = UserInfo.query();
        vm.quoteofthedays = QuoteOfTheDay.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.quoteOfTheDayHistory.id !== null) {
                QuoteOfTheDayHistory.update(vm.quoteOfTheDayHistory, onSaveSuccess, onSaveError);
            } else {
                QuoteOfTheDayHistory.save(vm.quoteOfTheDayHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:quoteOfTheDayHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
