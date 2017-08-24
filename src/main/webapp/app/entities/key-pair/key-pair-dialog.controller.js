(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('KeyPairDialogController', KeyPairDialogController);

    KeyPairDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'KeyPair', 'TrackMetricQuestion'];

    function KeyPairDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, KeyPair, TrackMetricQuestion) {
        var vm = this;

        vm.keyPair = entity;
        vm.clear = clear;
        vm.save = save;
        vm.trackmetricquestions = TrackMetricQuestion.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.keyPair.id !== null) {
                KeyPair.update(vm.keyPair, onSaveSuccess, onSaveError);
            } else {
                KeyPair.save(vm.keyPair, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:keyPairUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
