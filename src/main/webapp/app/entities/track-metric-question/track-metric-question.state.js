(function() {
    'use strict';

    angular
        .module('balancepositionApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('track-metric-question', {
            parent: 'entity',
            url: '/track-metric-question?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TrackMetricQuestions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/track-metric-question/track-metric-questions.html',
                    controller: 'TrackMetricQuestionController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('track-metric-question-detail', {
            parent: 'track-metric-question',
            url: '/track-metric-question/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TrackMetricQuestion'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/track-metric-question/track-metric-question-detail.html',
                    controller: 'TrackMetricQuestionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TrackMetricQuestion', function($stateParams, TrackMetricQuestion) {
                    return TrackMetricQuestion.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'track-metric-question',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('track-metric-question-detail.edit', {
            parent: 'track-metric-question-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/track-metric-question/track-metric-question-dialog.html',
                    controller: 'TrackMetricQuestionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TrackMetricQuestion', function(TrackMetricQuestion) {
                            return TrackMetricQuestion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('track-metric-question.new', {
            parent: 'track-metric-question',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/track-metric-question/track-metric-question-dialog.html',
                    controller: 'TrackMetricQuestionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                question: null,
                                isMandatory: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('track-metric-question', null, { reload: 'track-metric-question' });
                }, function() {
                    $state.go('track-metric-question');
                });
            }]
        })
        .state('track-metric-question.edit', {
            parent: 'track-metric-question',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/track-metric-question/track-metric-question-dialog.html',
                    controller: 'TrackMetricQuestionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TrackMetricQuestion', function(TrackMetricQuestion) {
                            return TrackMetricQuestion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('track-metric-question', null, { reload: 'track-metric-question' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('track-metric-question.delete', {
            parent: 'track-metric-question',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/track-metric-question/track-metric-question-delete-dialog.html',
                    controller: 'TrackMetricQuestionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TrackMetricQuestion', function(TrackMetricQuestion) {
                            return TrackMetricQuestion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('track-metric-question', null, { reload: 'track-metric-question' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
