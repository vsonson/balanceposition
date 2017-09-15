(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .controller('TrackMetricDialogController', TrackMetricDialogController);

    TrackMetricDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'TrackMetric', 'TrackMetricQuestion'];

    function TrackMetricDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, TrackMetric, TrackMetricQuestion) {
        var vm = this;

        vm.trackMetric = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
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
            if (vm.trackMetric.id !== null) {
                TrackMetric.update(vm.trackMetric, onSaveSuccess, onSaveError);
            } else {
                TrackMetric.save(vm.trackMetric, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('balancepositionApp:trackMetricUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setInfoBubble = function ($file, trackMetric) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        trackMetric.infoBubble = base64Data;
                        trackMetric.infoBubbleContentType = $file.type;
                    });
                });
            }
        };

    }
})();
