(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('TrackMetricQuestionDialogController', TrackMetricQuestionDialogController);

    TrackMetricQuestionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TrackMetricQuestion', 'KeyPair', 'TrackMetric'];

    function TrackMetricQuestionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TrackMetricQuestion, KeyPair, TrackMetric) {
        var vm = this;

        vm.trackMetricQuestion = entity;
        vm.clear = clear;
        vm.save = save;
        vm.keypairs = KeyPair.query();
        vm.trackmetrics = TrackMetric.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.trackMetricQuestion.id !== null) {
                TrackMetricQuestion.update(vm.trackMetricQuestion, onSaveSuccess, onSaveError);
            } else {
                TrackMetricQuestion.save(vm.trackMetricQuestion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:trackMetricQuestionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
